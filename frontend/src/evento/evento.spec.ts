import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventoComponent } from './evento';

describe('Evento', () => {
  let component: EventoComponent;
  let fixture: ComponentFixture<EventoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
