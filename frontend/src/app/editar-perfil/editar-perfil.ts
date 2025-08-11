import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth/auth';
import { EventoService } from '../evento-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-editar-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './editar-perfil.html',
  styleUrls: ['./editar-perfil.css']
})
export class EditarStudentComponent implements OnInit {
  private authService = inject(AuthService);
  private EventoService = inject(EventoService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  studentForm: FormGroup = this.fb.group({
    fullName: ['', [Validators.required]],
    cpf: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
    birthDate: ['', [Validators.required, this.datePastValidator]],
    phoneNumber: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
    degreeProgram: ['', [Validators.required]],
    currentPeriod: [1, [Validators.required, Validators.min(1), Validators.max(9)]]
  });

  ngOnInit() {
  const emailEstudante = this.authService.getUserEmail();
  if (emailEstudante) {
    this.EventoService.getEstudanteByEmail(emailEstudante).then((studentProfile) => {
      if (studentProfile) {
        this.studentForm.patchValue({
          fullName: studentProfile.fullName,
          cpf: studentProfile.cpf,
          birthDate: studentProfile.birthDate,
          phoneNumber: studentProfile.phoneNumber,
          degreeProgram: studentProfile.degreeProgram,
          currentPeriod: studentProfile.currentPeriod
        });
      } else {
        alert('Erro: Perfil do estudante não encontrado.');
      }
    }).catch(err => {
      alert('Erro ao carregar perfil: ' + err);
    });
  } else {
    alert('Erro: Email do estudante não encontrado.');
  }
}

  datePastValidator(control: AbstractControl) {
    const hoje = new Date();
    const valor = new Date(control.value);
    return valor > hoje ? { datafutura: true } : null;
  }

  onSubmit() {
    if (this.studentForm.valid) {
      const updatedProfile = this.studentForm.value;
      this.authService.updateStudent(updatedProfile)
        .then(() => {
          alert('Perfil atualizado com sucesso!');
          this.router.navigate(['/home']);
        })
        .catch(err => {
          alert('Erro ao atualizar perfil: ' + err);
        });
    }
  }

  onDelete() {
    const email = this.authService.getUserEmail(); // ajuste para seu método real
    console.log('Email do estudante:', email);
    if(email === null) {
      alert('Erro: Email do estudante não encontrado.');
      return;
    }
    if (confirm('Tem certeza que deseja deletar seu perfil? Esta ação é irreversível.')) {
      this.authService.deleteStudent(email)
        .then(result => {
          if (result) {
            alert('Perfil deletado com sucesso!');
            this.router.navigate(['/login']);
          } else {
            alert('Erro ao deletar perfil');
            this.router.navigate(['/login']);
          }
        })
        .catch(err => {
          console.error(err);
          alert('Erro ao deletar perfil');
          this.router.navigate(['/login']);
        });
    }
  }

}
