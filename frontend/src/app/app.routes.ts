import { Routes } from '@angular/router';
import { LoginComponent } from "./auth/login/login";
import { RegisterComponent } from "./auth/register/register";
import { authGuard } from './auth/auth-guard';
import { Unauthorized } from "./auth/unauthorized/unauthorized";
import { HomeComponent } from "./home/home";
import { EventoComponent } from './evento/evento';
import { RegisterStudentComponent } from './auth/register-student/register-student';
import { CriarEventoComponent } from './criar-evento/criar-evento';
import { eventDetails } from './ver-evento/ver-evento';
import { EditarEventoComponent } from './editar-evento/editar-evento';
import { VerInscricaoComponent } from './ver-inscricoes/ver-inscricoes';
import { EditarStudentComponent } from './editar-perfil/editar-perfil';

export const routes: Routes = [
    { 
        path: '', redirectTo: 'login', pathMatch: 'full'
    },
    { 
        path: 'login', component: LoginComponent, title: 'Login'
    },
    { 
        path: 'register/organizer', component: RegisterComponent, title: 'Cadastro de organizador' 
    },
    { 
        path: 'register/student', component: RegisterStudentComponent, title: 'Cadastro de estudante' 
    },
    {
        path: 'unauthorized', component: Unauthorized, title: 'Unauthorized'
    },
    { 
        path: 'home', component: HomeComponent, canActivate: [authGuard], title: 'Home' 
    },
    { 
        path: 'criar-evento', component: CriarEventoComponent, canActivate: [authGuard], title: 'Criar evento',
        data: { role: 'ORGANIZER' }
    },
    { 
        path: 'editar-estudante', component: EditarStudentComponent, canActivate: [authGuard], title: 'Editar perfil',
        data: { role: 'STUDENT' }
    },
    { 
        path: 'editar-evento/:id', component: EditarEventoComponent, canActivate: [authGuard], title: 'Editar evento',
        data: { role: 'ORGANIZER' }
    },
    { 
        path: 'details/:id', component: eventDetails, canActivate: [authGuard], title: 'Detalhes do evento',
        data: { role: 'STUDENT' }
    },
    { 
        path: 'editar-evento/:id/ver-inscricao', component: VerInscricaoComponent, canActivate: [authGuard], title: 'Detalhes do evento',
        data: { role: 'ORGANIZER' }
    },
    { 
        path: '**', redirectTo: 'login' 
    }
];
