import { Component, computed, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PlayerService } from '../../services/player.service';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="navigation">
      <div class="hamburger" (click)="toggleMenu()">
        <span></span>
        <span></span>
        <span></span>
      </div>
      <ul [class.active]="isMenuOpen()">
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
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 60px;
    }

    .hamburger {
      display: none;
      flex-direction: column;
      justify-content: space-between;
      width: 30px;
      height: 21px;
      cursor: pointer;
      padding: 0;
      position: absolute;
      right: 1rem;
      top: 50%;
      transform: translateY(-50%);
      z-index: 1001;
    }

    .hamburger span {
      display: block;
      width: 100%;
      height: 3px;
      background-color: #ecf0f1;
      transition: all 0.3s ease;
      border-radius: 3px;
    }

    .hamburger span:first-child {
      margin-bottom: 6px;
    }

    .hamburger span:last-child {
      margin-top: 6px;
    }

    .navigation ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      gap: 2rem;
      justify-content: center;
      align-items: center;
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

    @media (max-width: 768px) {
      .hamburger {
        display: flex;
      }

      .navigation ul {
        display: none;
        flex-direction: column;
        position: absolute;
        top: 100%;
        left: 0;
        right: 0;
        background-color: #2c3e50;
        padding: 1rem;
        gap: 1rem;
        z-index: 1000;
      }

      .navigation ul.active {
        display: flex;
      }

      .navigation li {
        width: 100%;
        text-align: center;
      }

      .navigation a {
        display: block;
        padding: 0.75rem;
      }
    }
  `]
})
export class NavigationComponent {
  isMenuOpen = signal(false);

  constructor(private playerService: PlayerService) {}

  isLoggedIn = computed(() => this.playerService.player !== null);

  toggleMenu(): void {
    this.isMenuOpen.update(value => !value);
  }
} 