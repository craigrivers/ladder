export interface Player {
    firstName: string;
    lastName: string;
    email: string;
    cell: string;
    level: number;
    ladderId:number;
    courtId:number;
    availability:string;
    password:string;    
    playerId:number;
    matchesWon: number;
    matchesLost: number;
    gamesWon: number;
    gamesLost: number;
    id: number;
    goesBy: string;
    phone: string;
    courtName: string;
}

export interface Ladder {
    ladderId:number;
    name:string;
    startDate:string;
    endDate:string;
}
export interface Court {
    courtId: number;
    name: string;
    address: string;
    link: string;
    phone: string;
}
export interface Match {
    matchId: number;
    ladderId: number;
    matchType: string;
    player1Id: number;
    player1Name: string;
    player2Id: number;
    player2Name: string;
    player3Id: number;
    player3Name: string;
    player4Id: number;
    player4Name: string;
    matchDate: string;
    courtId: number;
    matchScheduledStatus: string;
    courtName: string; }

    export interface SetScores {
        matchId: number;
        matchResultId: number;
        playerId: number;
        setScore: number;
        setNumber: number;
        setWinner: number; // 0 = loser, 1 = winner
    }
/*
    export interface MatchScores {
        matchResultId: number;
        matchId: number;
        player1Id: number;
        player2Id: number;
        player3Id: number;      
        player4Id: number;
        player1Score: number;
        player1SetWinner: number;       // 0 = loser, 1 = winner
        player2Score: number;
        player2SetWinner: number;
        player3Score: number;
        player3SetWinner: number;
        player4Score: number;
        player4SetWinner: number;
        set_number: number;
    }
*/
/*
export interface SetScoreForm {
    playerId: number;
    player1Score: number;
    player2Score: number;   
    setNumber: number;
    setScore: number;
    setWinner: number;
  }
*/

export interface MatchResultForm {
    matchResultId: number;
    matchId: number;    
    player1Id: number;
    player2Id: number;
    matchDate: string;
    courtId: number;
    matchWinnerId: number;
    setScores: SetScores[];
}
export interface Standing {
    playerId: number;
    firstName: string;
    lastName: string;   
    matchesWon: number;
    setsWon: number;
    gamesWon: number;
} 
