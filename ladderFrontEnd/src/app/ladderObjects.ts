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