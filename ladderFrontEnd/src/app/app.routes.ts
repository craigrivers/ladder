import { Routes } from '@angular/router';
import { StandingComponent } from './components/standing/standing.component';
import { RegisterComponent } from './components/register/register.component';
import { ReportScoresComponent } from './components/report-scores/report-scores.component';

export const routes: Routes = [
  { path: 'standing', component: StandingComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'report-scores', component: ReportScoresComponent },
  { path: '', redirectTo: '/standing', pathMatch: 'full' }
];
