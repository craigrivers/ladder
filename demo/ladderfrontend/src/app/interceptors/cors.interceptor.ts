import { HttpInterceptorFn, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const corsInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('CORS Interceptor - Original request:', {
    url: req.url,
    method: req.method,
    headers: req.headers.keys()
  });

  // Ensure we're not modifying the protocol
  const modifiedRequest = req.clone({
    withCredentials: true,
    headers: req.headers
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json')
  });

  console.log('CORS Interceptor - Modified request:', {
    url: modifiedRequest.url,
    method: modifiedRequest.method,
    headers: modifiedRequest.headers.keys()
  });
  
  return next(modifiedRequest).pipe(
    tap((event: HttpEvent<unknown>) => {
      if (event instanceof HttpResponse) {
        console.log('CORS Interceptor - Response:', {
          status: event.status,
          statusText: event.statusText,
          headers: event.headers.keys()
        });
      }
    }),
    catchError((error: HttpErrorResponse) => {
      console.error('CORS Interceptor - Error:', {
        status: error.status,
        statusText: error.statusText,
        message: error.message,
        headers: error.headers?.keys()
      });
      return throwError(() => error);
    })
  );
}; 