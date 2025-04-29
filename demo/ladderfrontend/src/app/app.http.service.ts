import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpUrlEncodingCodec } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { catchError, throwError } from 'rxjs';
import { Player, Court, Match } from './ladderObjects';

@Injectable()
export class HttpService {
  private http: HttpClient;
  private apiUrl: string = environment.apiUrl;

  constructor(http: HttpClient) {
    this.http = http;
  }

  register(player: Player): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log("JSON String = " + JSON.stringify(player));
    
    return this.http.post(
      `${this.apiUrl}register`,
      player,
      { headers }
    ).pipe(
      catchError((error: any) => {
        console.error('Error during registration:', error);
        return throwError(() => new Error(error.message || 'Registration failed'));
      })
    );
  }

  getStandings(ladderId: number): Observable<Player[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    
    return this.http.get<Player[]>(
      `${this.apiUrl}standings`,
      { params }
    ).pipe(
      catchError((error: any) => {
        console.error('Error fetching standings:', error);
        return throwError(() => new Error(error.message || 'Failed to fetch standings'));
      })
    );
  }

  login(email: string, password: string): Observable<Player> {
    const body = { email, password };
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post<Player>(
      `${this.apiUrl}login`,
      body,
      { headers }
    ).pipe(
      catchError((error: any) => {
        console.error('Error during login:', error);
        return throwError(() => new Error(error.message || 'Login failed'));
      })
    );
  } 

  getCourts(): Observable<Court[]> {
    return this.http.get<Court[]>(
      `${this.apiUrl}courts`
    ).pipe(
      catchError((error: any) => {
        console.error('Error fetching courts:', error);
        return throwError(() => new Error(error.message || 'Failed to fetch courts'));
      })
    );
  }

  scheduleMatch(match: Match): Observable<Match> {
    return this.http.post<Match>(
      `${this.apiUrl}addMatch`,
      match
    ).pipe(
      catchError((error: any) => {
        console.error('Error scheduling match:', error);
        return throwError(() => new Error(error.message || 'Failed to schedule match'));
      })
    );
  }

  updateMatch(match: Match): Observable<Match> {
    return this.http.post<Match>(
      `${this.apiUrl}updateMatch`,
      match
    ).pipe(
      catchError((error: any) => {
        console.error('Error updating match:', error);
        return throwError(() => new Error(error.message || 'Failed to update match'));
      })
    );
  }

  getPlayers(ladderId: number): Observable<Player[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    
    return this.http.get<Player[]>(
      `${this.apiUrl}players`,
      { params }
    ).pipe(
      catchError((error: any) => {
        console.error('Error fetching standings:', error);
        return throwError(() => new Error(error.message || 'Failed to fetch standings'));
      })
    );
  }

  updatePlayer(player: Player): Observable<Player> {
    return this.http.put<Player>(
      `${this.apiUrl}players`,
      player
    ).pipe(
      catchError((error: any) => {
        console.error('Error updating player:', error);
        return throwError(() => new Error(error.message || 'Failed to update player'));
      })
    );
  }

  getScheduledMatches(ladderId: number): Observable<Match[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    return this.http.get<Match[]>(
      `${this.apiUrl}scheduledMatches`,
      { params }
    ).pipe(
      catchError((error: any) => {
        console.error('Error fetching scheduled matches:', error);
        return throwError(() => new Error(error.message || 'Failed to fetch scheduled matches'));
      })
    );
  }

  private handleError(error: any) {
    if (error.status === 500) {
      return throwError(() => new Error('Internal Server Error'));
    }
    return throwError(() => new Error(error.message || error.text() || 'The server is not responding with data'));
  }

}