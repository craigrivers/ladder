import { Routes } from '@angular/router';
import { StandingComponent } from './components/standing/standing.component';
import { RegisterComponent } from './components/register/register.component';
import { ReportScoresComponent } from './components/report-scores/report-scores.component';
import { PlayersComponent } from './components/players/players.component';

export const routes: Routes = [
  { 
    path: 'standing', 
    component: StandingComponent,
    data: { page: 'standing' }
  },
  { 
    path: 'players', 
    component: PlayersComponent,
    data: { page: 'players' }
  },
  { path: 'register', component: RegisterComponent },
  { path: 'report-scores', component: ReportScoresComponent },
  { path: '', redirectTo: '/standing', pathMatch: 'full' }
];
