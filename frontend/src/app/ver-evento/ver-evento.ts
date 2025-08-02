import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { EventoService } from '../evento-service';
import { EventoComponent } from '../evento/evento';
import { EventoInterface } from '../eventoInterface';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ver-evento.html',
  styleUrls: ['./ver-evento.css']
})
export class eventDetails {
  route: ActivatedRoute = inject(ActivatedRoute);
  eventoService = inject (EventoService);
  evento: EventoInterface | undefined;
  applyForm = new FormGroup(
    {
      
    }
  );

  constructor() {
    const eventoId = Number(this.route.snapshot.params['id']);
    this.eventoService.getEventoById(eventoId)
     .then((evento) => {
      this.evento = evento;})
  };

  submitApplication() {
    /*this.housingService.submitApplication(
      this.applyForm.value.firstName ?? '',
      this.applyForm.value.lastName ?? '',
      this.applyForm.value.email ?? ''
    );*/
  }
}