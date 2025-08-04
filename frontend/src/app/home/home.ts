import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventoComponent } from '../evento/evento';
import { EventoService } from '../evento-service';
import { EventoInterface } from '../eventoInterface';
import { AuthService } from '../auth/auth';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, EventoComponent, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {
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

  ngOnInit() {
    this.eventoService.getAllEventos().then((EventoList) => {
      this.EventoList = EventoList;

      if (this.hasRole('ORGANIZER')) {
        console.log('Usuário é um organizador' + this.authService.getUserId());
        this.myEventList = EventoList.filter(evento => evento?.idOrganizer === parseInt(this.authService.getUserId() || '0', 10));
      } else {
        this.filteredEventoList = EventoList;
      }
    });
  }

  hasRole(role: string): boolean {
  return this.authService.hasRole(role);
  }

  filterResults(text: string) {
    if (!text) this.filteredEventoList = this.EventoList;

    this.filteredEventoList = this.EventoList.filter(
      evento => evento?.name.toLowerCase().includes(text.toLowerCase())
    );
  }

}