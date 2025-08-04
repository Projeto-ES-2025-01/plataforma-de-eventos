import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerInscricoes } from './ver-inscricoes';

describe('VerInscricoes', () => {
  let component: VerInscricoes;
  let fixture: ComponentFixture<VerInscricoes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerInscricoes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerInscricoes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
