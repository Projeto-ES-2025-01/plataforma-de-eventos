import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { EventoService } from '../evento-service';
import { EventoInterface } from '../eventoInterface';
import { AuthService } from '../auth/auth';
import { ReactiveFormsModule,} from '@angular/forms';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './ver-evento.html',
  styleUrls: ['./ver-evento.css']
})
export class eventDetails {
  route: ActivatedRoute = inject(ActivatedRoute);
  eventoService = inject(EventoService);
  authService = inject(AuthService);
  evento: EventoInterface | undefined;

  constructor() {
  
    const eventoId = Number(this.route.snapshot.params['id']);
    this.eventoService.getEventoById(eventoId).then((evento) => {
      this.evento = evento;
    });
  }

  eventApplication() {
  const email = this.authService.getUserEmail();
  if (!email) {
    alert('Por favor, informe seu e-mail.');
    return;
  }

  const eventoId = Number(this.route.snapshot.params['id']);
  this.eventoService.getEstudanteByEmail(email)
    .then((studentProfile) => {
      if (!studentProfile) {
        alert('Estudante não encontrado.');
        return;
      }

      this.eventoService.submitApplication(eventoId, studentProfile)
        .then(() => {
          alert('Inscrição realizada com sucesso!');
        })
        .catch((err) => {
          console.error(err);
        });
    })
    .catch((err) => {
      console.error(err);
      alert('Erro ao buscar estudante.');
    });
  }
}
