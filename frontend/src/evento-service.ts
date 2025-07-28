import { Injectable } from '@angular/core';
import { EventoInterface } from './app/eventoInterface';

@Injectable({
  providedIn: 'root'
})
export class EventoService {

  url = 'http://localhost:3000/eventos'

  constructor() { }

  async getAllEventos(): Promise<EventoInterface[]> {
    const data = await fetch(this.url);
    return data.json() ?? [];
  }

  async getEventoById(id: number): Promise<EventoInterface | undefined> {
    const data = await fetch(`${this.url}/${id}`);
    return data.json() ?? {};
  }

  submitApplication(firstName: string, lastName: string, email: string) {
    console.log(`Application submitted for ${firstName} ${lastName} at ${email}`);
  }

  async addEvento(EventoInterface: EventoInterface) {
    const response = await fetch(this.url, {
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