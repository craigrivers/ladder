import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
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
    ReactiveFormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [HttpService]
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
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
    courtName: '',
    receiveText: false
  };

  courts: Court[] = [];
  isLoading = true;
  error: string | null = null;
  currentPlayer: Player | null = null;

  constructor(
    public httpService: HttpService,
    private router: Router,
    private playerService: PlayerService,
    private fb: FormBuilder
  ) {
    this.currentPlayer = this.playerService.player;
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&_])[A-Za-z\d@$!%*?&_]{8,}$/)
      ]],
      cell: [''],
      courtId: ['', Validators.required],
      availability: [''],
      receiveText: [false]
    });
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
    if (this.registerForm.valid) {
      this.isLoading = true;
      const formValue = this.registerForm.value;
      this.player = { ...this.player, ...formValue };
      this.player.email = this.player.email.toLowerCase();
      
      this.httpService.register(this.player).subscribe({
        next: (response) => {
          console.log('Registration successful:', this.player.firstName + ' ' + this.player.lastName);
          this.playerService.setPlayer(this.player);
          this.router.navigate(['/players']);
        },
        error: (error) => {
          console.error('Registration failed:', error);
          this.error = 'Registration failed. Please try again.';
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }
} 