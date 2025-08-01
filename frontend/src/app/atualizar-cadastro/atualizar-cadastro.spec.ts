import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizarCadastro } from './atualizar-cadastro';

describe('AtualizarCadastro', () => {
  let component: AtualizarCadastro;
  let fixture: ComponentFixture<AtualizarCadastro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AtualizarCadastro]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AtualizarCadastro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
