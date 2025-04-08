import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Player } from '../../ladderObjects';
import { HttpService } from '../../app.http.service';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [HttpService]
})
export class RegisterComponent {
  player: Player = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    skillLevel: 1
  };

  constructor (public httpService: HttpService){

  }

  onSubmit() {
    // TODO: Implement the registration logic here
    console.log('Form submitted:', this.player);
  this.httpService.register(this.player);
  }
} 