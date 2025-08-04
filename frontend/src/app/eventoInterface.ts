export interface EventoInterface {
    id: number,
    idOrganizer: number,
    name: string,
    location: string,
    date: string,
    time: string,
    description: string
}

export interface CriarEventoInterface {
    idOrganizer: number,
    name: string,
    location: string,
    date: string,
    time: string,
    description: string
}