import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../app.http.service';
import { Standing } from '../../ladderObjects';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-standing',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './standing.component.html',
  styleUrls: ['./standing.component.css'],
  providers: [HttpService]
})
export class StandingComponent implements OnInit {
  standings: Standing[] = [];
  isLoading = true;
  error: string | null = null;
  http: HttpService;

  constructor(http: HttpService) {
    this.http = http;
  }

  ngOnInit(): void {
    this.loadStandings();
  }

  loadStandings(): void {
    // Using ladderId 1 as an example - you might want to make this dynamic
    this.http.getStandings(1).subscribe({
      next: (data) => {
        this.standings = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load standings. Please try again later.';
        this.isLoading = false;
        console.error('Error loading standings:', err);
      }
    });
  }
} 