import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Player } from '../../ladderObjects';
import { HttpService } from '../../app.http.service';

@Component({
  selector: 'app-players',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css' ],
  providers: [HttpService]
})
export class PlayersComponent implements OnInit {
  players: Player[] = [];
  isLoading = true;
  error: string | null = null;
  http: HttpService;
  page: string = '';

  constructor(http: HttpService, private route: ActivatedRoute) {
    this.http = http;
  }

  ngOnInit(): void {
    this.page = this.route.snapshot.data['page'];
    this.loadPlayers();
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
} 