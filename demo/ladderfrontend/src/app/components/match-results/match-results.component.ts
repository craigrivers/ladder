import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataTablesModule } from 'angular-datatables';
import { MatchResult, MatchResultForm, Player } from '../../ladderObjects';
import { Subject } from 'rxjs';
import { DataTableDirective } from 'angular-datatables';
import { HttpService } from '../../app.http.service';
import { Api } from 'datatables.net';

@Component({
  selector: 'app-match-results',
  standalone: true,
  imports: [CommonModule, DataTablesModule],
  providers: [HttpService],
  templateUrl: './match-results.component.html',
  styleUrls: ['./match-results.component.css']
})
export class MatchResultsComponent implements OnInit, AfterViewInit, OnDestroy {
  matchResults: MatchResult[] = [];
  dtOptions: any = {};
  dtTrigger: Subject<any> = new Subject<any>();
  ladderId: number = 1;

  @ViewChild(DataTableDirective, { static: false })
  dtElement!: DataTableDirective;

  constructor(private httpService: HttpService) {
    this.ladderId = 1;
  }

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
    this.loadMatchResults();   
  }

  loadMatchResults() {
    this.httpService.getMatchResults(this.ladderId).subscribe((matchResults: MatchResultForm[]) => {
      this.matchResults = matchResults.map(matchResult => this.getMatchResult(matchResult));
      // Only trigger DataTable after data is set
      this.dtTrigger.next(null);
    });
  }

  ngAfterViewInit(): void {
    // Do not trigger here; trigger after data is loaded
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  /** 
   * This function will generate the MatchResult object which is used to display the match results in the table.
   * @param matchResultForm 
   * @returns MatchResult object
   */
  getMatchResult(matchResultForm: MatchResultForm): MatchResult {
    let matchScore = this.getFormattedScores(matchResultForm);
    let players = this.getPlayers(matchResultForm); 
    return {
      players: players,
      matchDate: matchResultForm.matchDate,
      courtName: matchResultForm.courtName,
      matchWinner: matchResultForm.winnerName,
      matchScore: matchScore
    };
  }
  
  /**
  * This function will generate the players string which is used to display the players in the table.
  * @param matchResultForm 
  * @returns Players string
  */
  getPlayers(matchResult: MatchResultForm): string {
    let players = "";
    if (matchResult.matchWinnerId === matchResult.player1Id) { 
      players = matchResult.player1Name + " vs " + matchResult.player2Name;
    } else {
      players = matchResult.player2Name + " vs " + matchResult.player1Name;
    }
    return players;
  }
  
  /**
   * This function will generate scores where the match winners score will be displayed first, followed by the loser's score.
   * Examples: 6-4, 2-6, 6-4 or 6-4, 6-4
   * @param matchResultForm 
   * @returns 6-4, 2-6, 6-4 or 6-4, 6-4
   */
    getFormattedScores(matchResultForm: MatchResultForm) :string {
    let message = "";
    let loserSetScore = null;
    let winnerSetScore = null;
    let winner_player_id = matchResultForm.matchWinnerId;

    // Generating the scores in the order of the sets
    matchResultForm.setScores.sort((a, b) => a.setNumber - b.setNumber);
    for (let setScore of matchResultForm.setScores) {
      if (setScore.setNumber != 3) {
            if (setScore.playerId === winner_player_id) {
                winnerSetScore = setScore.setScore;
            } else {
                loserSetScore = setScore.setScore;
            }
            if (loserSetScore != null && winnerSetScore != null) {
                message += winnerSetScore.toString() + "-" + loserSetScore.toString() ;
                if (setScore.setNumber === 1){
                  message += ", ";
                } 
                loserSetScore = null;
                winnerSetScore = null;
            }
        } else {
          //  Third set
            if (setScore.playerId === winner_player_id) {
                winnerSetScore = setScore.setScore;
            } else {
                loserSetScore = setScore.setScore;
            }
            if (loserSetScore != null && winnerSetScore != null) {
                // Do not display 0-0 for the third set
                if (!(winnerSetScore === 0 && loserSetScore === 0)) {
                    message += ", " + winnerSetScore.toString() + "-" + loserSetScore.toString();
                }
                loserSetScore = null;
                winnerSetScore = null;
            }
        }
    }
    return message;
  } 

  rerender(): void {
    if (this.dtElement && this.dtElement.dtInstance) {
      this.dtElement.dtInstance.then((dtInstance: Api) => {
        dtInstance.destroy();
        this.dtTrigger.next(null);
      });
    }
  }
}