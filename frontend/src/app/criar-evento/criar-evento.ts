import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EventoService } from '../evento-service';
import { AuthService } from '../auth/auth';
import { Router, RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-criar-evento',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './criar-evento.html',
  styleUrl: './criar-evento.css'
})
export class CriarEventoComponent implements OnInit {
  eventoForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private eventoService: EventoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.eventoForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      time: ['', Validators.required],
      date: ['', [Validators.required, this.dateFutureValidator]],
      description: ['', [Validators.required, Validators.minLength(50)]],
    });
  }

  dateFutureValidator(control: AbstractControl) {
  const hoje = new Date();
  const valor = new Date(control.value);
  return valor < hoje ? { datapassada: true } : null;
}

  onSubmit(): void {
  if (this.eventoForm.valid) {
    const formValue = this.eventoForm.value;

    const { date, time, name, location, description } = formValue;
    const [datePart] = date.split('T');
    const [year, month, day] = datePart.split('-');
    const dateTimeString = `${year}-${month}-${day}`;
    const userId = parseInt(this.authService.getUserId() || '0', 10);

    const eventoFinal = {
      idOrganizer: userId,
      //id: 0,
      name,
      location,
      date: dateTimeString,
      time,
      description
    };

    this.eventoService.addEvento(eventoFinal)
      .then(() => {
        alert('Evento criado com sucesso!');
        this.router.navigate(['/home']);
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
