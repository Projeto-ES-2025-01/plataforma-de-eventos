import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizarEvento } from './atualizar-evento';

describe('AtualizarEvento', () => {
  let component: AtualizarEvento;
  let fixture: ComponentFixture<AtualizarEvento>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AtualizarEvento]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AtualizarEvento);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
