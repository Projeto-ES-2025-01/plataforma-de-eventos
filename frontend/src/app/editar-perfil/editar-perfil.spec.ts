import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarStudentComponent } from './editar-perfil';

describe('EditarPerfil', () => {
  let component: EditarStudentComponent;
  let fixture: ComponentFixture<EditarStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarStudentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
