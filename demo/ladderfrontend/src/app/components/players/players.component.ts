import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Match, MatchScores, Player, Court } from '../../ladderObjects';
import { HttpService } from '../../app.http.service';
import { PlayerService } from '../../services/player.service';

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
  matchScores: MatchScores[] = [];
  updatedAvailability: string = '';

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
    private playerService: PlayerService
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
        this.scheduledMatches = data;
      },
      error: (err) => {
        this.error = 'Failed to load scheduled matches. Please try again later.';
        console.error('Error loading scheduled matches:', err);
      }
    });
  }

  loadScheduledMatchesStatic(): void {
    this.scheduledMatches.push({
      matchId: 1,
      player1Id: 1,
      player1Name: 'Player 1',
      player2Id: 2,
      player2Name: 'Player 2',
      player3Id: 3,
      player3Name: 'Player 3',
      player4Id: 4,
      player4Name: 'Player 4',
      matchDate: '2021-01-01',
      courtName: 'Court 1',
      ladderId: 1,
      matchType: 'Singles',
      courtId: 1,
      matchScheduledStatus: 'Scheduled',
    }); 
    this.scheduledMatches.push({
      matchId: 2,
      player1Id: 1,
      player1Name: 'Player 1',
      player2Id: 2,
      player2Name: 'Player 2',
      player3Id: 3,
      player3Name: 'Player 3',
      player4Id: 4,
      player4Name: 'Player 4',
      matchDate: '2021-01-01',
      courtName: 'Court 2',
      ladderId: 1,
      matchType: 'Singles',
      courtId: 2,
      matchScheduledStatus: 'Scheduled',
    });
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

    this.newMatch.ladderId = 1; // Assuming ladderId 1 for now
    this.newMatch.matchType = 'Singles'; // Default to singles for now
    this.newMatch.matchScheduledStatus = 'Scheduled';

    this.http.scheduleMatch(this.newMatch).subscribe({
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

    const updatedPlayer = { ...this.currentPlayer, availability: this.updatedAvailability };
    
    this.http.updatePlayer(updatedPlayer).subscribe({
      next: (response) => {
        console.log('Availability updated successfully:', response);
        this.currentPlayer = response;
        this.playerService.setPlayer(response);
        this.loadPlayers(); // Refresh the players list
      },
      error: (err) => {
        this.error = 'Failed to update availability. Please try again later.';
        console.error('Error updating availability:', err);
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
} 