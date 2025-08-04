import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventoService } from '../evento-service';
import { StudentProfileDTO } from '../auth/user';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ver-participantes',
  templateUrl: './ver-inscricoes.html',
  imports: [CommonModule],
  styleUrls: ['./ver-inscricoes.css'],
  standalone: true,
})
export class VerInscricaoComponent {
  route = inject(ActivatedRoute);
  eventoService = inject(EventoService);

  participantes: StudentProfileDTO[] = [];
  filteredParticipantesName: StudentProfileDTO[] = [];
  filteredParticipantesCpf: StudentProfileDTO[] = [];


  ngOnInit() {
    const idEvento = Number(this.route.snapshot.params['id']);
    this.eventoService.getAllStudents(idEvento).then((participante) => {
      this.participantes = participante;
      console.log('Participantes:', this.participantes);
      this.filteredParticipantesName = participante;
    });
  }

  filterResultsCpf(text: string): void {
    if (!text) {
      this.filteredParticipantesCpf = this.participantes;
      return;
    }

    this.filteredParticipantesCpf = this.participantes.filter(
      participante => participante.cpf.toLowerCase().includes(text.toLowerCase())
    );
  }

  filterResultsName(text: string): void {
    if (!text) {
      this.filteredParticipantesName = this.participantes;
      return;
    }

    this.filteredParticipantesName = this.participantes.filter(
      participante => participante.fullName.toLowerCase().includes(text.toLowerCase())
    );
  }
}
