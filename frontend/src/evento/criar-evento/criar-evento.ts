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
    private eventoService: EventoService,
    private authService: AuthService 
  ) {}

  ngOnInit(): void {
    this.eventoForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      time: ['', Validators.required],
      date: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit(): void {
  if (this.eventoForm.valid) {
    const formValue = this.eventoForm.value;

    const { date, time, name, location, description } = formValue;
    const [datePart] = date.split('T');
    const [year, month, day] = datePart.split('-');
    const dateTimeString = `${day}/${month}/${year}`;
    const dateTime = new Date(dateTimeString);
    const userId = parseInt(this.authService.getUserId() || '0', 10);

    const eventoFinal = {
      id: userId,
      name,
      location,
      date: dateTimeString,
      time: dateTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      description
    };

    this.eventoService.addEvento(eventoFinal)
      .then(() => {
        alert('Evento criado com sucesso!');
        this.eventoForm.reset();
      })
      .catch(error => {
        console.error('Erro ao criar evento:', error);
        alert('Erro ao criar evento');
      });
  } else {
    alert('Formulário inválido!');
  }
}
}
