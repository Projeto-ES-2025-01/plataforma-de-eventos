import { Injectable } from '@angular/core';
import { EventoInterface } from './eventoInterface';

@Injectable({
  providedIn: 'root'
})
export class EventoService {

  url = 'http://localhost:8080'

  constructor() { }

  async getAllEventos(): Promise<EventoInterface[]> {
    const data = await fetch(this.url + '/event/allEvents');
    return data.json() ?? [];
  }

  async searchEventos(searchTerm: string): Promise<EventoInterface[]> {
  const response = await fetch(`${this.url}/event/search`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name: searchTerm }) // Send the search term as JSON
  });

  if (!response.ok) {
    throw new Error('Erro ao buscar eventos');
  }

  return response.json() ?? [];
}

  async getEventoById(id: number): Promise<EventoInterface | undefined> {
    const data = await fetch(`${this.url}/event/get/${id}`);
    return data.json() ?? {};
  }

  submitApplication(firstName: string, lastName: string, email: string) {
    console.log(`Application submitted for ${firstName} ${lastName} at ${email}`);
  }

  async addEvento(EventoInterface: EventoInterface) {
    const response = await fetch(this.url + '/event/create', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(EventoInterface)
  });
    const data = await response.json();
    console.log(data);
  }
}