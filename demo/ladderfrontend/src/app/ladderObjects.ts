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

export interface Standing {
    firstName: string;
    lastName: string;
    goesBy: string;
    matchesWon: number;
    matchesLost: number;
    gamesWon: number;
    gamesLost: number;
    ladderId:number;
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

    export interface MatchScores {
        matchId: number;
        player1Id: number;
        player2Id: number;
        player3Id: number;      
        player4Id: number;
        player1Score: number;
        player2Score: number;
        player3Score: number;
        player4Score: number;
        set_number: number;
        

    }
