import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EventoService } from '../evento-service';
import { EventoInterface } from '../eventoInterface';
import { AuthService } from '../auth/auth';

@Component({
  standalone: true,
  selector: 'app-editar-evento',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './editar-evento.html',
  styleUrl: './editar-evento.css'
})
export class EditarEventoComponent implements OnInit {
  eventoForm!: FormGroup;
  eventoId!: number;

  constructor(
    private fb: FormBuilder,
    private eventoService: EventoService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.eventoId = Number(this.route.snapshot.params['id']);

    this.eventoForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      time: ['', Validators.required],
      date: ['', [Validators.required, this.dateFutureValidator]],
      description: ['', [Validators.required, Validators.minLength(50)]],
    });

    this.carregarEvento();
  }

  dateFutureValidator(control: AbstractControl) {
  const hoje = new Date();
  const valor = new Date(control.value);
  return valor < hoje ? { datapassada: true } : null;
}

  async carregarEvento() {
    try {
    const evento = await this.eventoService.getEventoById(this.eventoId);
    const dateParts = (evento?.date ?? '').split('/');
    const [day, month, year] = dateParts.length === 3 ? dateParts : ['', '', ''];
    const dateFormatted = `${year}-${month}-${day}`;

    this.eventoForm.patchValue({
      name: evento?.name,
      location: evento?.location,
      time: evento?.time,
      date: dateFormatted,
      description: evento?.description,
      idOrganizer: evento?.idOrganizer
    });
  } catch (error) {
    console.error('Erro ao carregar evento:', error);
  }
  }

  async onSubmit() {
    if (this.eventoForm.valid) {
      const { name, location, time, date, description, idOrganizer } = this.eventoForm.value;
      const [year, month, day] = date.split('-');
      const dateFormatted = `${year}-${month}-${day}`;
      const timeFormatted = time?.substring(0, 5);
      const idOrganizerNumber = Number(this.authService.getUserId());

      const eventoAtualizado: EventoInterface = {
        id: this.eventoId,
        idOrganizer: idOrganizerNumber,
        name,
        location,
        time: timeFormatted,
        date: dateFormatted,
        description
      };

      try {
        await this.eventoService.updateEvento(eventoAtualizado, this.eventoId);
        alert('Evento atualizado com sucesso!');
        this.router.navigate(['/home']); 
      } catch (error) {
        console.error('Erro ao atualizar evento:', error);
        alert('Erro ao atualizar evento');
      }
    } else {
      alert('Formulário inválido!');
    }
  }

  async onDelete() {
    if (confirm('Tem certeza que deseja deletar este evento? Esta ação é irreversível.')) {
      try {
        await this.eventoService.deleteEvento(this.eventoId);
        alert('Evento deletado com sucesso!');
        this.router.navigate(['/home']);
      } catch (error) {
        console.error('Erro ao deletar evento:', error);
        alert('Erro ao deletar event');
      }
    }
  }
}
