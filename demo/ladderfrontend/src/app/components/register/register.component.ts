import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { Player, Court } from '../../ladderObjects';
import { HttpService } from '../../app.http.service';
import { CommonModule } from '@angular/common';
import { PlayerService } from '../../services/player.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [HttpService]
})
export class RegisterComponent implements OnInit {
  player: Player = {
    firstName: '',
    lastName: '',
    email: '',
    cell: '',
    level: 1,
    ladderId: 1,
    courtId: 0,
    availability: '',
    password: '',
    playerId: 0,
    matchesWon: 0,
    matchesLost: 0,
    gamesWon: 0,
    gamesLost: 0,
    id: 0,
    goesBy: '',
    phone: '',
    courtName: ''
  };

  courts: Court[] = [];
  isLoading = true;
  error: string | null = null;
  currentPlayer: Player | null = null;

  constructor (
    public httpService: HttpService,
    private router: Router,
    private playerService: PlayerService
  ) 
  {
    this.currentPlayer = this.playerService.player;
  }
  
  ngOnInit(): void {
    this.loadCourts();
  }

  loadCourts(): void {
    this.httpService.getCourts().subscribe({
      next: (data) => {
        this.courts = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load courts. Please try again later.';
        this.isLoading = false;
        console.error('Error loading courts:', err);
      }
    });
  }

  onSubmit() {
    this.player.email = this.player.email.toLowerCase();
    console.log('Form submitted:', this.player);
    this.httpService.register(this.player).subscribe({
      next: (response) => {
        console.log('Registration successful:', this.player.firstName + ' ' + this.player.lastName );
        this.playerService.setPlayer(this.player);
        this.router.navigate(['/players']);
      },
      error: (error) => {
        console.error('Registration failed:', error);
      }
    });
  }
} 