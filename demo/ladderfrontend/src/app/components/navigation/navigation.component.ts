import { Component, computed } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PlayerService } from '../../services/player.service';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="navigation">
      <ul>
        <li>
          <a routerLink="/home" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Home</a>
        </li>
        <li *ngIf="!isLoggedIn()">
          <a routerLink="/register" routerLinkActive="active">Register</a>
        </li> 
        <li *ngIf="isLoggedIn()">
          <a routerLink="/players" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Schedule Matches</a>
        </li>
        <li *ngIf="isLoggedIn()">
          <a routerLink="/update-availability" routerLinkActive="active">Update Availability</a>
        </li>
        <li *ngIf="isLoggedIn()">
          <a routerLink="/report-scores" routerLinkActive="active">Report Scores</a>
        </li>
        <li *ngIf="isLoggedIn()">
          <a routerLink="/standing" routerLinkActive="active">Standings</a>
        </li>
      </ul>
    </nav>
  `,
  styles: [`
    .navigation {
      background-color: #2c3e50;
      padding: 1rem;
      margin-bottom: 2rem;
    }

    .navigation ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      gap: 2rem;
      justify-content: center;
    }

    .navigation a {
      color: #ecf0f1;
      text-decoration: none;
      font-weight: 500;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      transition: background-color 0.3s ease;
    }

    .navigation a:hover {
      background-color: #34495e;
    }

    .navigation a.active {
      background-color: #3498db;
    }
  `]
})
export class NavigationComponent {
  constructor(private playerService: PlayerService) {}

  isLoggedIn = computed(() => this.playerService.player !== null);
} 