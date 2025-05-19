import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Match, MatchScores, SetScores, Player, Court } from '../../ladderObjects';
import { HttpService } from '../../app.http.service';
import { PlayerService } from '../../services/player.service';

interface SetScoreForm {
  player1Score: number;
  player2Score: number;
  setNumber: number;
}

// Utility to format date for datetime-local input
function toDatetimeLocalString(date: string | Date): string {
  const d = typeof date === 'string' ? new Date(date) : date;
  if (isNaN(d.getTime())) return '';
  const pad = (n: number) => n.toString().padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

function noChange(date: string): string {
  return date;
}

@Component({
  selector: 'app-report-scores',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './report-scores.component.html',
  styleUrls: ['./report-scores.component.scss'],
  providers: [HttpService]
})
export class ReportScoresComponent implements OnInit {
  players: Player[] = [];
  courts: Court[] = [];
  isLoading = true;
  error: string | null = null;
  http: HttpService;
  page: string = '';
  currentPlayer: Player | null = null;
  
  scheduledMatches: Match[] = [];
  myScheduledMatches: Match[] = [];
  matchScores: MatchScores[] = [];
  player2: Player | null = null;
/*
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
*/  
  matchForm: FormGroup;
  setScores: SetScoreForm[] = [
    { player1Score: 0, player2Score: 0, setNumber: 1 },
    { player1Score: 0, player2Score: 0, setNumber: 2 },
    { player1Score: 0, player2Score: 0, setNumber: 3 }
  ];

 // setScoresDb : SetScores[] = [];

  constructor(
    http: HttpService,
    private route: ActivatedRoute,
    private playerService: PlayerService,
    //private router: Router,
    private fb: FormBuilder
  ) {
    this.http = http;
    this.currentPlayer = this.playerService.player;
    
    this.matchForm = this.fb.group({
      player1Id: [this.currentPlayer?.playerId || '', Validators.required],
      player2Id: ['', Validators.required],
      matchDate: [new Date().toISOString().slice(0, 16), Validators.required],
      courtId: [''],
      setScores: this.fb.array(this.setScores.map(set => this.fb.group({
        player1Score: [set.player1Score, [Validators.required, Validators.min(0), Validators.max(7)]],
        player2Score: [set.player2Score, [Validators.required, Validators.min(0), Validators.max(7)]]
      })))
    });
  }

  ngOnInit(): void {
    this.page = this.route.snapshot.data['page'];
    this.loadPlayers();
    this.loadCourts();
    this.loadScheduledMatches();
    
    // Initialize newMatch with current player's information
    if (this.currentPlayer) {
 //     this.newMatch.player1Id = this.currentPlayer.playerId;
 //     this.newMatch.player1Name = `${this.currentPlayer.firstName} ${this.currentPlayer.lastName}`;
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
  //  this.newMatch.matchDate = `${year}-${month}-${day}T${hours}:${minutes}`;
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

  report(): void {
    /*
    if (!this.newMatch.player1Id || !this.newMatch.player2Id || !this.newMatch.courtId || !this.newMatch.matchDate) {
      this.error = 'Please fill in all required fields.';
      return;
    }
      */

    if (!this.currentPlayer) {
      this.error = 'No player is logged in ';
      return;
    }

    if (this.players.length === 0) {
      this.error = 'Players data is not yet loaded. Please try again in a moment.';
      return;
    }

    /*
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
    */
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
      },
      error: (err) => {
        this.error = 'Failed to update match. Please try again later.';
        console.error('Error updating match:', err);
      }
    });
  }
/*
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
*/

  onSubmit(): void {
    if (this.matchForm.valid) {
      const formValue = this.matchForm.value;

      const setScoresDb = this.transferSetScoresToDb(formValue.setScores, formValue);
      console.log('Set Scores DB:', setScoresDb);

      // Create MatchResult object
      const matchResult = {
        player1Id: formValue.player1Id,
        player2Id: formValue.player2Id,
        matchDate: new Date(formValue.matchDate),
        matchWinnerId: this.determineMatchWinner(formValue.setScores),
        setScoresDb: this.transferSetScoresToDb(formValue.setScores, formValue),
        setScores: formValue.setScores.map((set: SetScoreForm, index: number) => {
          const setWinner = set.player1Score > set.player2Score ? 1 : 2;
          return {
          //  playerId: setWinner === 1 ? formValue.player1Id : formValue.player2Id,
            player1Score: set.player1Score,
            player2Score: set.player2Score, 
            setNumber: index + 1
          };
        })
      };

      // TODO: Implement API call to save match result
      console.log('Match Result:', matchResult);
    }
  }

  private determineMatchWinner(setScores: SetScoreForm[]): number {
    let player1Wins = 0;
    let player2Wins = 0;

    setScores.forEach(set => {
      if (set.player1Score > set.player2Score) player1Wins++;
      if (set.player2Score > set.player1Score) player2Wins++;
    });

    return player1Wins > player2Wins ? this.matchForm.get('player1Id')?.value : this.matchForm.get('player2Id')?.value;
  }

  get setScoresArray() {
    return this.matchForm.get('setScores') as FormArray;
  }

  get player2Name(): string {
    const player2Id = this.matchForm.get('player2Id')?.value;
    const player = this.players.find(p => p.playerId === player2Id);
    return player ? `${player.firstName} ${player.lastName}` : '';
  }

  get setScoreFormGroups(): FormGroup[] {
    return (this.setScoresArray.controls as FormGroup[]);
  }
/*
 *  After getting the set scores from the form, this function transfers the setScores to the setScoresDb object
 *  The setScoresDB object is an array of objects that contain the set scores for each player. For each set, there are two objects in the array, one for each player.
 */
  transferSetScoresToDb(setScores: SetScoreForm[], formValue: any): SetScores[] {
    const setScoresDb: SetScores[] = [];  
    setScores.map((set: SetScoreForm, index: number) => {
      const setWinner = set.player1Score > set.player2Score ? 1 : 2;
      setScoresDb.push({ 
        playerId: formValue.player1Id,
        score: set.player1Score,
        set_number: index + 1,
        setWinner: set.player1Score > set.player2Score ? 1 : 0,    
        matchId: formValue.matchId}, 
        {
        playerId: formValue.player2Id,
        score: set.player2Score,
        set_number: index + 1,
        setWinner: set.player2Score > set.player1Score ? 1 : 0,
        matchId: formValue.matchId
        });
    });
    return setScoresDb;
  }
        

} 