import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Match, SetScores, Player, Court } from '../../ladderObjects';
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
  
  matchForm: FormGroup;
  setScores: SetScoreForm[] = [
    { player1Score: 0, player2Score: 0, setNumber: 1 },
    { player1Score: 0, player2Score: 0, setNumber: 2 },
    { player1Score: 0, player2Score: 0, setNumber: 3 }
  ];

  constructor(
    http: HttpService,
    private route: ActivatedRoute,
    private playerService: PlayerService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.http = http;
    this.currentPlayer = this.playerService.player;
    
    this.matchForm = this.fb.group({
      player1Id: [this.currentPlayer?.playerId || '', Validators.required],
      player2Id: ['', Validators.required],
      matchDate: [toDatetimeLocalString(new Date()) , Validators.required],
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
 
    if (!this.currentPlayer) {
      this.error = 'No player is logged in ';
      return;
    }

    if (this.players.length === 0) {
      this.error = 'Players data is not yet loaded. Please try again in a moment.';
      return;
    }
  }
  
  onSubmit(): void {
    if (this.matchForm.valid) {
      //Disable the submit button
      this.matchForm.disable();
      const formValue = this.matchForm.value;

      const setScoresDb = this.transferSetScoresToDb(formValue.setScores, formValue);
      console.log('Set Scores DB:', setScoresDb);

      // Create MatchResult object
      const matchResult = {
        matchId: 0,
        matchResultId: 0,
        ladderId: 1,
        player1Id: formValue.player1Id,
        player2Id: formValue.player2Id,
        matchDate: new Date(formValue.matchDate),
        matchWinnerId: this.determineMatchWinner(formValue.setScores),
        setScores: setScoresDb,
        courtId: formValue.courtId
      };

      // Call the service to save the match result
      this.http.saveMatchResult(matchResult).subscribe({
        next: (response) => {
          console.log('Match result saved successfully:', response);
          this.router.navigate(['/standing']);
          // Handle success (e.g., show success message, reset form, etc.)
        },
        error: (error) => {
          console.error('Error saving match result:', error);
          this.error = 'Failed to save match result. Please try again.';
        },
        complete: () => {
          //Enable the submit button
          this.matchForm.enable();
        }
      });
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

  get player1Name(): string {
    const player1Id = this.matchForm.get('player1Id')?.value;
    const player = this.players.find(p => p.playerId === player1Id);
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
        matchResultId: formValue.matchResultId,
        playerId: formValue.player1Id,
        setScore: set.player1Score,
        setNumber: index + 1,
        setWinner: set.player1Score > set.player2Score ? 1 : 0,    
        matchId: formValue.matchId}, 
        {
        matchResultId: formValue.matchResultId, 
        playerId: formValue.player2Id,
        setScore: set.player2Score,
        setNumber: index + 1,
        setWinner: set.player2Score > set.player1Score ? 1 : 0,
        matchId: formValue.matchId
        });
    });
    return setScoresDb;
  }
}  