import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpUrlEncodingCodec } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { catchError, throwError } from 'rxjs';
import { Player, Standing, Court } from './ladderObjects';

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
      'http://localhost:8080/ladder/register',
      player,
      { headers }
    ).pipe(
      catchError((error: any) => {
        console.error('Error during registration:', error);
        return throwError(() => new Error(error.message || 'Registration failed'));
      })
    );
  }

  getStandings(ladderId: number): Observable<Standing[]> {
    const params = new HttpParams().set('ladderId', ladderId.toString());
    
    return this.http.get<Standing[]>(
      'http://localhost:8080/ladder/standings',
      { params }
    ).pipe(
      catchError((error: any) => {
        console.error('Error fetching standings:', error);
        return throwError(() => new Error(error.message || 'Failed to fetch standings'));
      })
    );
  }

  getCourts(): Observable<Court[]> {
    return this.http.get<Court[]>(
      'http://localhost:8080/ladder/courts'
    ).pipe(
      catchError((error: any) => {
        console.error('Error fetching courts:', error);
        return throwError(() => new Error(error.message || 'Failed to fetch courts'));
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