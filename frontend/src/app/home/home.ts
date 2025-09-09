import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventoComponent } from '../evento/evento';
import { EventoService } from '../evento-service';
import { EventoInterface } from '../eventoInterface';
import { AuthService } from '../auth/auth';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, EventoComponent, RouterModule,],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {
  router = inject(Router);
  authService = inject(AuthService);
  EventoList: EventoInterface[] = [];
  eventoService: EventoService = inject (EventoService);
  filteredEventoList: EventoInterface[] = [];
  myEventList: EventoInterface[] = [];

  constructor() {
    this.eventoService.getAllEventos().then((EventoList) => {
      this.EventoList = EventoList;
      this.filteredEventoList = EventoList;
    });
  }

  async ngOnInit() {
    const email = this.authService.getUserEmail() || '';
    const idOrganizer = await this.eventoService.getOrganizerByEmail(email);
    console.log('ID do Organizador: ' + idOrganizer);

    this.eventoService.getAllEventos().then((EventoList) => {
      this.EventoList = EventoList;
      if (!email) {
        console.error('Email do usuário não encontrado.');
        return;
      }
      if (this.hasRole('ORGANIZER')) {
        console.log('Usuário é um organizador: ' + this.authService.getUserId());
        this.myEventList = EventoList.filter(evento => evento?.idOrganizer === parseInt(idOrganizer));
      } else {
        this.filteredEventoList = EventoList;
      }
    });
  }

  hasRole(role: string): boolean {
  return this.authService.hasRole(role);
  }
  //teste do .yml

  filterResults(text: string, startDate?: string, endDate?: string) {
  this.filteredEventoList = this.EventoList.filter(evento => {
    const matchesText = !text || evento?.name.toLowerCase().includes(text.toLowerCase());

    const eventoDate = new Date(evento.date);
    const start = startDate ? new Date(startDate) : null;
    const end = endDate ? new Date(endDate) : null;

    const matchesStart = !start || eventoDate >= start;
    const matchesEnd = !end || eventoDate <= end;

    return matchesText && matchesStart && matchesEnd;
  });
}
}