import { ComponentFixture, TestBed } from '@angular/core/testing';

import { eventDetails } from './ver-evento';

describe('VerEvento', () => {
  let component: eventDetails;
  let fixture: ComponentFixture<eventDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [eventDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(eventDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
