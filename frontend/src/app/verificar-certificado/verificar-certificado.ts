import { Component, inject } from '@angular/core';
import { EventoService } from '../evento-service';  
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-verificar-certificado',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './verificar-certificado.html',
  styleUrl: './verificar-certificado.css'
})
export class VerificarCertificado {
  private eventoService = inject(EventoService);

  certificateCode: string = '';
  certificateValid: boolean | null = null;

  verifyCertificate() {
  console.log('Sending code:', this.certificateCode);
  this.certificateValid = null;
  this.eventoService.verifyCertificate(this.certificateCode)
    .then(result => {
      this.certificateValid = result === true;
    })
    .catch(error => {
      console.error(error);
      this.certificateValid = false;
    });
}
}