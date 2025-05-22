import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Match,  Player, Court } from '../../ladderObjects';
import { HttpService } from '../../app.http.service';
import { PlayerService } from '../../services/player.service';

// Utility to format date for datetime-local input
function toDatetimeLocalString(date: string | Date): string {
  let d: Date;
  if (typeof date === 'string') {
    // Create date object from UTC string
    d = new Date(date);
  } else {
    d = date;
  }
  
  console.log('Parsed date:', d);
  if (isNaN(d.getTime())) {
    console.log('Invalid date detected');
    return '';
  }
  
  const pad = (n: number) => n.toString().padStart(2, '0');
  // Use UTC time directly
  const result = `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getUTCHours())}:${pad(d.getUTCMinutes())}`;
  return result;
}

function noChange(date: string): string {
  return date;
}

@Component({
  selector: 'app-players',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css' ],
  providers: [HttpService]
})
export class PlayersComponent implements OnInit {
  players: Player[] = [];
  courts: Court[] = [];
  isLoading = true;
  error: string | null = null;
  http: HttpService;
  page: string = '';
  currentPlayer: Player | null = null;
  
  scheduledMatches: Match[] = [];
  myScheduledMatches: Match[] = [];
  updatedAvailability: string = '';
  player2: Player | null = null;

  newMatch: Match = { 
    matchId: 0,
    player1Id: 0,
    player1Name: '',
    player2Id: 0,
    player2Name: '',
    player3Id: 0, 
    player3Name: '',
    player4Id: 0,
    player4Name: '',
    matchDate: '',
    courtName: '',
    ladderId: 0,
    matchType: '',
    courtId: 0,
    matchScheduledStatus: ''
  };

  constructor(
    http: HttpService, 
    private route: ActivatedRoute,
    private playerService: PlayerService,
    private router: Router
  ) {
    this.http = http;
    this.currentPlayer = this.playerService.player;
  }

  ngOnInit(): void {
    this.page = this.route.snapshot.data['page'];
    this.loadPlayers();
    this.loadCourts();
    this.loadScheduledMatches();
    
    // Initialize newMatch with current player's information
    if (this.currentPlayer) {
      this.newMatch.player1Id = this.currentPlayer.playerId;
      this.newMatch.player1Name = `${this.currentPlayer.firstName} ${this.currentPlayer.lastName}`;
      this.updatedAvailability = this.currentPlayer.availability;
    }

    // Set default match date to current date + 2 days in EST
    const defaultDate = new Date();
    defaultDate.setDate(defaultDate.getDate() + 2);
    
    // Format the date in EST (UTC-5)
    const year = defaultDate.getFullYear();
    const month = String(defaultDate.getMonth() + 1).padStart(2, '0');
    const day = String(defaultDate.getDate()).padStart(2, '0');
    const hours = String(defaultDate.getHours()).padStart(2, '0');
    const minutes = String(defaultDate.getMinutes()).padStart(2, '0');
    
    // Format as YYYY-MM-DDTHH:mm for datetime-local input
    this.newMatch.matchDate = `${year}-${month}-${day}T${hours}:${minutes}`;
  }

  loadCourts(): void {
    this.http.getCourts().subscribe({
      next: (data) => {
        this.courts = data;
      },
      error: (err) => {
        this.error = 'Failed to load courts. Please try again later.';
        console.error('Error loading courts:', err);
      }
    });
  }

  loadScheduledMatches(): void {
    this.http.getScheduledMatches(1).subscribe({
      next: (data) => {
        // Ensure each match has the correct courtId and formatted date
        this.scheduledMatches = data.map(match => {
          const court = this.courts.find(c => c.name === match.courtName);
          if (court) {
            match.courtId = court.courtId;
          }
          match.matchDate = toDatetimeLocalString(match.matchDate);
          return match;
        });
        // filter out matches that were scheduled to happen in the past or have already happened
        this.scheduledMatches = this.scheduledMatches.filter(match => {
          const matchDate = new Date(match.matchDate);
          return matchDate > new Date();
        });
        // Order the scheduled matches by matchDate
        this.scheduledMatches.sort((a, b) => {
          const dateA = new Date(a.matchDate);
          const dateB = new Date(b.matchDate);
          return dateA.getTime() - dateB.getTime();
        });
        this.loadMyScheduledMatches();
      },
      error: (err) => {
        this.error = 'Failed to load scheduled matches. Please try again later.';
        console.error('Error loading scheduled matches:', err);
      }
    });
  }

  loadMyScheduledMatches(): void {
    // Use the scheduledMatches array to find any matches that have the current player's id in the player1Id or player2Id field
    this.myScheduledMatches = this.scheduledMatches
      .filter(match => match.player1Id === this.currentPlayer?.playerId || match.player2Id === this.currentPlayer?.playerId)
      .map(match => ({ ...match, matchDate: noChange(match.matchDate) }));
  }

  loadPlayers(): void {
    this.http.getPlayers(1).subscribe({
      next: (data) => {
        this.players = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load players. Please try again later.';
        this.isLoading = false;
        console.error('Error loading players:', err);
      }
    });
  }

  scheduleMatch(): void {
    if (!this.newMatch.player1Id || !this.newMatch.player2Id || !this.newMatch.courtId || !this.newMatch.matchDate) {
      this.error = 'Please fill in all required fields.';
      return;
    }

    if (!this.currentPlayer) {
      this.error = 'No player is logged in ';
      return;
    }

    if (this.players.length === 0) {
      this.error = 'Players data is not yet loaded. Please try again in a moment.';
      return;
    }

    this.newMatch.ladderId = 1; // Assuming ladderId 1 for now
    this.newMatch.matchType = 'Singles'; // Default to singles for now
    this.newMatch.matchScheduledStatus = 'scheduled';

    this.player2 = this.players.find(
      p => Number(p.playerId) === Number(this.newMatch.player2Id)
    ) || null;
    if (!this.player2) {
      this.error = 'Selected player not found. Please try again.';
      return;
    }

    this.newMatch.player2Name = this.player2?.firstName + ' ' + this.player2?.lastName || '';
    this.newMatch.courtName = this.courts.find(c => Number(c.courtId) === Number(this.newMatch.courtId))?.name || ''; 

    this.http.scheduleMatchSendNotification(this.newMatch, "add-match", this.currentPlayer).subscribe({ 
      next: (response) => {
        console.log('Match scheduled successfully:', response);
        this.loadScheduledMatches();
        this.resetNewMatch();
      },
      error: (err) => {
        this.error = 'Failed to schedule match. Please try again later.';
        console.error('Error scheduling match:', err);
      }
    });
  }

  updateAvailability(): void {
    if (!this.currentPlayer) {
      this.error = 'No player selected';
      return;
    }
    console.log('Updated availability:', this.updatedAvailability);
    const updatedPlayer = { ...this.currentPlayer, availability: this.updatedAvailability };
    this.http.updatePlayer(updatedPlayer).subscribe({
      next: (response) => {
        console.log('Availability updated successfully:', response);
        this.currentPlayer = updatedPlayer;
        this.playerService.setPlayer(updatedPlayer);
        this.loadPlayers(); // Refresh the players list
        this.router.navigate(['/players']);
      },
      error: (err) => {
        this.error = 'Failed to update availability. Please try again later.';
        console.error('Error updating availability:', err);
      }
    });
  }

  updateMatch(match: Match): void {
    // Update courtName based on selected courtId
    const selectedCourt = this.courts.find(court => Number(court.courtId) === Number(match.courtId));
    if (selectedCourt) {
      match.courtName = selectedCourt.name;
    }

    this.http.updateMatch(match).subscribe({
      next: (response) => {
        console.log('Match updated successfully:', response);
        this.loadScheduledMatches();
      },
      error: (err) => {
        this.error = 'Failed to update match. Please try again later.';
        console.error('Error updating match:', err);
      }
    });
  }

  private resetNewMatch(): void {
    this.newMatch = {
      matchId: 0,
      player1Id: this.currentPlayer?.playerId || 0,
      player1Name: this.currentPlayer?.firstName + ' ' + this.currentPlayer?.lastName || '',
      player2Id: 0,
      player2Name: '',
      player3Id: 0,
      player3Name: '',
      player4Id: 0,
      player4Name: '',
      matchDate: '',
      courtName: '',
      ladderId: 0,
      matchType: '',
      courtId: 0,
      matchScheduledStatus: ''
    };
  }

  sendNotification(match:Match, notificationType: string): void {
    if (!this.currentPlayer) {
      this.error = 'User is not logged in';
      console.error('Error sending notification:', this.error);
      return;
    }
    this.http.sendNotification(match, notificationType, this.currentPlayer).subscribe({
      next: (response) => {
        alert('Email sent successfully');
      },
      error: (err) => {
        this.error = 'Failed to send email. Please try again later.';
        console.error('Error sending email:', err);
      }
    });
  }   
}