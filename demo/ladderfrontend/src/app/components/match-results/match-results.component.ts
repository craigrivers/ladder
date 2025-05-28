import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataTablesModule } from 'angular-datatables';
import { MatchResult } from '../../ladderObjects';
import { Subject } from 'rxjs';
import { DataTableDirective } from 'angular-datatables';

@Component({
  selector: 'app-match-results',
  standalone: true,
  imports: [CommonModule, DataTablesModule],
  template: `
    <div class="match-results-container">
      <h2>Match Results</h2>
      <table datatable [dtOptions]="dtOptions" [dtTrigger]="dtTrigger" class="row-border hover">
        <thead>
          <tr>
            <th>Players</th>
            <th>Match Date</th>
            <th>Court</th>
            <th>Winner</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let result of matchResults">
            <td>{{ result.players }}</td>
            <td>{{ result.matchDate | date:'mediumDate' }}</td>
            <td>{{ result.courtName }}</td>
            <td>{{ result.matchWinner }}</td>
            <td>{{ result.matchScore }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: [`
    .match-results-container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }

    h2 {
      color: #2c3e50;
      margin-bottom: 20px;
    }

    :host ::ng-deep {
      .dataTables_wrapper {
        .dataTables_length, .dataTables_filter {
          margin-bottom: 15px;
        }

        .dataTables_info, .dataTables_paginate {
          margin-top: 15px;
        }

        table.dataTable {
          width: 100% !important;
          margin: 15px 0;
          border-collapse: collapse;
          
          thead th {
            background-color: #2c3e50;
            color: white;
            padding: 12px;
            text-align: left;
          }

          tbody td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
          }

          tbody tr:hover {
            background-color: #f5f5f5;
          }
        }
      }
    }
  `]
})
export class MatchResultsComponent implements OnInit, AfterViewInit, OnDestroy {
  matchResults: MatchResult[] = [];
  dtOptions: any = {};
  dtTrigger: Subject<any> = new Subject<any>();

  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      processing: true,
      responsive: true,
      order: [[1, 'desc']],
      language: {
        search: 'Search:',
        lengthMenu: 'Show _MENU_ entries',
        info: 'Showing _START_ to _END_ of _TOTAL_ entries',
        infoEmpty: 'Showing 0 to 0 of 0 entries',
        infoFiltered: '(filtered from _MAX_ total entries)',
        zeroRecords: 'No matching records found',
        paginate: {
          first: 'First',
          last: 'Last',
          next: 'Next',
          previous: 'Previous'
        }
      }
    };

    // Example data
    this.matchResults = [
      {
        players: 'John Doe vs Jane Smith',
        matchDate: '2024-03-15',
        courtName: 'Main Court',
        matchWinner: 'John Doe',
        matchScore: '6-4, 6-3'
      }
    ];
  }

  ngAfterViewInit(): void {
    this.dtTrigger.next(null);
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
} 