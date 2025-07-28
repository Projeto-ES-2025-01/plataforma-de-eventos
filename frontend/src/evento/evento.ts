import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventoInterface } from '../app/eventoInterface';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-evento',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './evento.html',
  styleUrls: ['./evento.css']
})
export class EventoComponent {
  @Input() evento!: EventoInterface;
}