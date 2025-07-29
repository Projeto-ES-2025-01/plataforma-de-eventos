import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EventoService } from '../../evento-service';
import { AuthService } from '../../app/auth/auth';
import { EventoInterface } from '../../app/eventoInterface';

@Component({
  standalone: true,
  selector: 'app-criar-evento',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './criar-evento.html',
  styleUrl: './criar-evento.css'
})
export class CriarEventoComponent implements OnInit {
  eventoForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private eventoService: EventoService
  ) {}

  ngOnInit(): void {
    this.eventoForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      time: ['', Validators.required],
      photo: [''],
      organizer: ['', Validators.required],
      description: ['', Validators.required],
      numberOfParticipants: [0],
      maxParticipants: [1, Validators.required]
    });
  }

  async onSubmit() {
    if (this.eventoForm.invalid) {
      return;
    }

    const novoEvento: EventoInterface = this.eventoForm.value;
    await this.eventoService.addEvento(novoEvento);
    alert('Evento criado com sucesso!');
    this.eventoForm.reset();
  }
}
