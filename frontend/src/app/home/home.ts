import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventoComponent } from '../../evento/evento';
import { EventoService } from '../../evento-service';
import { EventoInterface } from '../eventoInterface';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, EventoComponent],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {
  EventoList: EventoInterface[] = [];
  eventoService: EventoService = inject (EventoService);
  filteredEventoList: EventoInterface[] = [];

  constructor() {
    this.eventoService.getAllEventos().then((EventoList) => {
      this.EventoList = EventoList;
      this.filteredEventoList = EventoList;
    });
  }

  filterResults(text: string) {
    if (!text) this.filteredEventoList = this.EventoList;

    this.filteredEventoList = this.EventoList.filter(
      evento => evento?.location.toLowerCase().includes(text.toLowerCase())
    );
  }
}