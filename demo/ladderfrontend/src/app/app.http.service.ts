import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpUrlEncodingCodec } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable, tap } from 'rxjs';
import { catchError, throwError } from 'rxjs';
import { Player, Court, Match } from './ladderObjects';

@Injectable()
export class HttpService {
  private http: HttpClient;
  private apiUrl: string = environment.apiUrl;

  constructor(http: HttpClient) {
    this.http = http;
    console.log('HTTP Service initialized with API URL:', this.apiUrl);
  }

  private getHeaders(): HttpHeaders {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json');
    
    console.log('Generated headers:', headers.keys());
    return headers;
  }

  private logRequest(method: string, url: string, headers: HttpHeaders, body?: any) {
    console.log(`Making ${method} request to:`, url);
    console.log('Request headers:', headers.keys());
    console.log('Request body:', body);
  }

  private logResponse(response: any) {
    console.log('Response received:', response);
  }

  register(player: Player): Observable<any> {
    console.log("JSON String = " + JSON.stringify(player));
    
    return this.http.post(
      `${this.apiUrl}register`,
      player,
      { 
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }

  getStandings(ladderId: number): Observable<Player[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    
    return this.http.get<Player[]>(
      `${this.apiUrl}standings`,
      { 
        params,
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }

  login(email: string, password: string): Observable<Player> {
    const body = { email, password };
    return this.http.post<Player>(
      `${this.apiUrl}login`,
      body,
      { 
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  } 

  getCourts(): Observable<Court[]> {
    const headers = this.getHeaders();
    this.logRequest('GET', `${this.apiUrl}courts`, headers);
    
    return this.http.get<Court[]>(
      `${this.apiUrl}courts`,
      { 
        headers,
        withCredentials: true
      }
    ).pipe(
      tap(response => this.logResponse(response)),
      catchError(error => {
        console.error('Error fetching courts:', error);
        return this.handleError(error);
      })
    );
  }
/*
  scheduleMatch(match: Match): Observable<Match> {
    return this.http.post<Match>(
      `${this.apiUrl}addMatch`,
      match,
      { 
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }
*/
  scheduleMatchSendNotification(match:Match, notificationType: string, loginPlayer: Player): Observable<Match> {
    console.log("scheduleMatchSendNotification:" + " match: " + "player2: "+ match.player2Name  + " player1: " + match.player1Name + " court: " + match.courtName); 
    const body = { match, notificationType, loginPlayer };
    return this.http.post<Match>(
      `${this.apiUrl}updateMatch/sendNotification`,
      body,
      { 
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }


  updateMatch(match: Match): Observable<Match> {
    return this.http.post<Match>(
      `${this.apiUrl}updateMatch`,
      match,
      { 
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }

  getPlayers(ladderId: number): Observable<Player[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    
    return this.http.get<Player[]>(
      `${this.apiUrl}players`,
      { 
        params,
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }

  updatePlayer(player: Player): Observable<Player> {
    return this.http.put<Player>(
      `${this.apiUrl}players`,
      player,
      { 
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }

  getScheduledMatches(ladderId: number): Observable<Match[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    return this.http.get<Match[]>(
      `${this.apiUrl}scheduledMatches`,
      { 
        params,
        headers: this.getHeaders(),
        withCredentials: true
      }
    ).pipe(
      catchError(this.handleError)
    );
  }
/*
  sendEmail(email: string, subject: string, message: string): Observable<any> {
    const body = { email, subject, message };
    return this.http.post(`${this.apiUrl}sendEmail`, body, {
      headers: this.getHeaders(),
      withCredentials: true
    }).pipe(  
      catchError(this.handleError)
    );
  }
*/
  sendNotification(match:Match, notificationType: string, loginPlayer: Player): Observable<any> {
    const body = { match, notificationType, loginPlayer };
    return this.http.post(`${this.apiUrl}sendNotification`, body, {
      headers: this.getHeaders(),
      withCredentials: true
    }).pipe(  
      catchError(this.handleError)
    );
  }

  private handleError(error: any) {
    console.error('API Error:', error);
    if (error.status === 0) {
      return throwError(() => new Error('Unable to connect to the server. Please check your network connection.'));
    }
    if (error.status === 500) {
      return throwError(() => new Error('Internal Server Error'));
    }
    return throwError(() => new Error(error.message || error.text() || 'The server is not responding with data'));
  }
}