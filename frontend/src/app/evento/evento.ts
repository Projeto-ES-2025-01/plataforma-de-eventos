import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventoInterface } from '../eventoInterface';
import { RouterModule } from '@angular/router';
import { AuthService } from '../auth/auth';

@Component({
  selector: 'app-evento',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './evento.html',
  styleUrls: ['./evento.css']
})
export class EventoComponent {
  @Input() evento!: EventoInterface;
  authService = inject(AuthService);

  hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }
}