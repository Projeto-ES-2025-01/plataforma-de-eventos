import { Injectable } from '@angular/core';
import { CriarEventoInterface, EventoInterface } from './eventoInterface';
import { StudentProfileDTO } from './auth/user';
import { environment } from '../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class EventoService {

  apiUrl = environment.apiUrl;

  constructor() { }

  async getAllEventos(): Promise<EventoInterface[]> {
    const data = await fetch(this.apiUrl + '/event/allEvents');
    return data.json() ?? [];
  }

  async searchEventos(searchTerm: string): Promise<EventoInterface[]> {
  const response = await fetch(`${this.apiUrl}/event/search`, {
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
    const data = await fetch(`${this.apiUrl}/event/get/${id}`);
    return data.json() ?? {};
  }

  async getAllStudents(id: number): Promise<StudentProfileDTO[]> {
    const data = await fetch(`${this.apiUrl}/event/AllParticipants/${id}`);
    return data.json() ?? []; // Ensure it returns an empty array if no data is found
}

  async getEstudanteByEmail(email: string): Promise<StudentProfileDTO | undefined> {
    const data = await fetch(`${this.apiUrl}/student/getProfile/${email}`);
    return data.json() ?? {};
  }

  async getOrganizerByEmail(email: string): Promise<string> {
    const data = await fetch(`${this.apiUrl}/organizer/getId/${email}`);
    return data.json() ?? {};
  }

  async submitApplication( eventoId: number, StudentProfileDTO: StudentProfileDTO): Promise<any> {
    const response = await fetch(`${this.apiUrl}/student/joinEvent/${eventoId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(StudentProfileDTO)
  });
    const data = await response.json();
    console.log(data);
  }

  async unsubmitApplication( eventoId: number, StudentProfileDTO: StudentProfileDTO): Promise<any> {
    const response = await fetch(`${this.apiUrl}/student/leaveEvent/${eventoId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(StudentProfileDTO)
  });
    const data = await response.json();
    console.log(data);
  }

  async addEvento(CriarEventoInterface: CriarEventoInterface) : Promise<any> {
    const response = await fetch(this.apiUrl + '/event/create', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(CriarEventoInterface)
  });
    const data = await response.json();
    console.log(data);
  }

  async updateEvento(EventoInterface: EventoInterface, eventoId: number) : Promise<any> {
    const response = await fetch(`${this.apiUrl}/event/update/${eventoId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(EventoInterface)
  });
    const data = await response.json();
    console.log(data);
  }

  async deleteEvento(eventoId: number): Promise<void> {
    const response = await fetch(`${this.apiUrl}/event/delete/${eventoId}`, {
      method: "DELETE"
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar evento');
    }
  }

  async confirmParticipation(eventoId: number, studentEmail: string): Promise<void> {
    const response = await fetch(`${this.apiUrl}/student/confirmparticipation/${studentEmail}/${eventoId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({})
    });
    if (!response.ok) {
      throw new Error('Erro ao confirmar participação');
    }
  }

  async generateCertificate(eventoId: number): Promise<void> {
    const response = await fetch(`${this.apiUrl}/certificate/sendAll/${eventoId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({})
    });
  }

  async getCertificateId(studentEmail: string, eventoId: number): Promise<Number| null> {
    const response = await fetch(`${this.apiUrl}/certificate/getByParticipantEventId/${eventoId}/${studentEmail}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Erro ao buscar ID do certificado');
    }
    return response.json();
  }

  async getCertificate(CertificateId: number): Promise<Blob> {
    const response = await fetch(`${this.apiUrl}/certificate/${CertificateId}/pdf`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Erro ao buscar certificado');
    }
    return response.blob();
  }

  async verifyCertificate(certificateCode: string): Promise<boolean> {
    const response = await fetch(`${this.apiUrl}/certificate/verify/${certificateCode}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) {
      throw new Error('Erro ao verificar certificado');
    }
    if (response.status === 200) {
      return true;
    }
    else {
      return false;
    }
  }

  async downloadCertificate(studentEmail: string, eventoId: number) {
  try {
    // Primeiro, obtenha o ID do certificado
    const certificateId = await this.getCertificateId(studentEmail, eventoId);
    if (!certificateId) {
      alert('Certificado não encontrado.');
      return;
    }
    // Agora, obtenha o PDF como Blob
    const pdfBlob = await this.getCertificate(Number(certificateId));
    // Crie uma URL para o Blob
    const url = window.URL.createObjectURL(pdfBlob);
    // Crie um link temporário para download
    const a = document.createElement('a');
    a.href = url;
    a.download = 'certificado.pdf';
    document.body.appendChild(a);
    a.click();
    // Limpe o link e a URL
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    alert('Erro ao baixar certificado');
    console.error(error);
  }
  }
}
  
