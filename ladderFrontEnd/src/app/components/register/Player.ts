export class Player {
    name: string;
    email: string;
    cell: string;
    availability: string;

    constructor(name: string, email: string, cell: string, availability: string) {
        this.name = name;
        this.email = email;
        this.cell = cell;
        this.availability = availability;
    }
}
