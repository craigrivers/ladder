import { Routes } from '@angular/router';
import { StandingComponent } from './components/standing/standing.component';
import { RegisterComponent } from './components/register/register.component';
import { ReportScoresComponent } from './components/report-scores/report-scores.component';
import { PlayersComponent } from './components/players/players.component';
import { LandingComponent } from './components/landing/landing.component';
import { HomeComponent } from './components/home/home.component';
import { MatchResultsComponent } from './components/match-results/match-results.component';

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
  { 
    path: 'update-availability', 
    component: PlayersComponent,
    data: { page: 'update-availability' }
  },
  { path: 'register', component: RegisterComponent },
  { path: 'report-scores', component: ReportScoresComponent },
  { path: 'match-results', component: MatchResultsComponent },
  { path: 'home', component: HomeComponent },
  { path: '', component: LandingComponent }
];
