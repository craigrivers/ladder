import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { HttpService } from '../../app.http.service';
import { Player } from '../../ladderObjects';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  providers: [HttpService],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent {
  readonly email = signal<string>('');
  readonly password = signal<string>('');
  errorMessage = signal<string | null>(null);
  isLoading = signal<boolean>(false);

  constructor(
    private router: Router,
    private playerService: PlayerService,
    private httpService: HttpService
  ) {}

  onLogin(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    // TODO: Replace with actual login API call
    
    console.log("Login request received for email: " + this.email() );
    this.httpService.login(this.email().toLowerCase(), this.password()).subscribe({
      next: (player: Player) => {
        if (player) {
          this.playerService.setPlayer(player);
          this.router.navigate(['/players']);
        } else {
          this.errorMessage.set('Invalid email or password');
        }
        this.isLoading.set(false);
      },
      error: (error) => {
        this.errorMessage.set('Failed to login. Please try again later.');
        this.isLoading.set(false);
        console.error('Login error:', error);
      }
    });
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
} 