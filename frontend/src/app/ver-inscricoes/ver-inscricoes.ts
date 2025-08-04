import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventoService } from '../evento-service';
import { StudentProfileDTO } from '../auth/user';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-ver-participantes',
  templateUrl: './ver-inscricoes.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./ver-inscricoes.css'],
  standalone: true,
})
export class VerInscricaoComponent {
  route = inject(ActivatedRoute);
  eventoService = inject(EventoService);

  participantes: StudentProfileDTO[] = [];
  participantesFiltrados: StudentProfileDTO[] = [];

  filterName: string = '';
  filterCpf: string = '';

  ngOnInit() {
    const idEvento = Number(this.route.snapshot.params['id']);
    this.eventoService.getAllStudents(idEvento).then((participante) => {
      this.participantes = participante;
      this.participantesFiltrados = participante;
    });
  }

  filtrar(): void {
    this.participantesFiltrados = this.participantes.filter(p => {
      const nomeCond = this.filterName
        ? p.fullName.toLowerCase().includes(this.filterName.toLowerCase())
        : true;
      const cpfCond = this.filterCpf
        ? p.cpf.toLowerCase().includes(this.filterCpf.toLowerCase())
        : true;
      return nomeCond && cpfCond;
    });
  }
}
