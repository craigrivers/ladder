import { Injectable, signal } from '@angular/core';
import { Player } from '../ladderObjects';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  private readonly playerSignal = signal<Player | null>(null);

  get player() {
    return this.playerSignal();
  }

  setPlayer(player: Player) {
    this.playerSignal.set(player);
  }

  clearPlayer() {
    this.playerSignal.set(null);
  }
} 