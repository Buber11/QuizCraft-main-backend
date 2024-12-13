import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthGuard } from './auth.guard';
import { AuthService } from './auth-service.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('AuthGuard', () => {
  let authGuard: AuthGuard;
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        AuthGuard,
        {
          provide: AuthService,
          useValue: { isLoggedIn: () => of(true) }
        }
      ]
    });

    authGuard = TestBed.inject(AuthGuard);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(authGuard).toBeTruthy();
  });

  it('should allow navigation if user is logged in', (done) => {
    spyOn(authService, 'isLoggedIn').and.returnValue(of(true));
    authGuard.canActivate(null, null).subscribe(result => {
      expect(result).toBe(true);
      done();
    });
  });

  it('should not allow navigation if user is not logged in', (done) => {
    spyOn(authService, 'isLoggedIn').and.returnValue(of(false));
    spyOn(router, 'navigate');

    authGuard.canActivate(null, null).subscribe(result => {
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
      done();
    });
  });
});
