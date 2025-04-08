import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpUrlEncodingCodec } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { catchError, throwError } from 'rxjs';
import { Player } from './ladderObjects';

@Injectable()
export class HttpService {

  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  register(player : Player){

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');
    console.log("JSON String = " +  JSON.stringify(player));
    return this.http.post(this.apiUrl + 'pcc/update.nei', JSON.stringify(player), {headers: headers})
      .pipe(catchError(this.handleError));

  }

  private handleError(error: any) {
    if (error.status === 500) {
      return throwError('Internal Server Error');
    }
    return throwError(error.message || error.text() || 'The server is not responding with data');
  }

}