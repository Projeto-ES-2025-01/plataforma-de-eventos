import { Injectable } from '@angular/core';
import { CriarEventoInterface, EventoInterface } from './eventoInterface';
import { StudentProfileDTO } from './auth/user';

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
    body: JSON.stringify({ name: searchTerm })
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

  async getAllStudents(id: number): Promise<StudentProfileDTO[]> {
    const data = await fetch(`${this.url}/event/AllParticipants/${id}`);
    return data.json() ?? []; // Ensure it returns an empty array if no data is found
}

  async getEstudanteByEmail(email: string): Promise<StudentProfileDTO | undefined> {
    const data = await fetch(`${this.url}/student/getProfile/${email}`);
    return data.json() ?? {};
  }

  async submitApplication( eventoId: number, StudentProfileDTO: StudentProfileDTO): Promise<any> {
    const response = await fetch(`${this.url}/student/joinEvent/${eventoId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(StudentProfileDTO)
  });
    const data = await response.json();
    console.log(data);
  }

  async addEvento(CriarEventoInterface: CriarEventoInterface) : Promise<any> {
    const response = await fetch(this.url + '/event/create', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(CriarEventoInterface)
  });
    const data = await response.json();
    console.log(data);
  }

  async updateEvento(EventoInterface: EventoInterface, eventoId: number) {
    const response = await fetch(`${this.url}/event/update/${eventoId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(EventoInterface)
  });
    const data = await response.json();
    console.log(data);
  }
}