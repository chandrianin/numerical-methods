import matplotlib.pyplot as plt
import math

CONST_A = 0.1
CONST_B = 3.1
CONST_N = 100
CONST_RATE = 10 ** -7


def f(x):
    return 2 * math.log(x) - (x / 2) + 1


def df(x):
    return 2 / x - 1 / 2


def ddf(x):
    return -2 / x ** 2


# Построение графика
def graph():
    X = []
    Y = []
    for x in [i / 100.0 for i in
              range(int(CONST_A * 100), int(CONST_B * 100), int(100 * (CONST_A + CONST_B) / CONST_N))]:
        X.append(x)
        Y.append(f(x))
    plt.plot(X, Y)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.grid()
    plt.show()


# Метод Ньютона (касательных)
def newton():
    def next_x(x_k):
        return x_k - f(x_k) / df(x_k)

    x = [CONST_A, next_x(CONST_A)]
    while abs(x[-1] - x[-2]) >= CONST_RATE:
        x.append(next_x(x[-1]))
    print("Метод Ньютона (касательных):")
    print("".join(format(i, '.7f') + "\n" for i in x))


# Метод хорд
def chords():
    a = CONST_A
    b = CONST_B

    x = []
    if f(b) * ddf(b) > 0:
        x.append(a)
        x_k = x[-1]
        x.append(x_k - (b - x_k) * f(x_k) / (f(b) - f(x_k)))
    else:
        x.append(b)
        x_k = x[-1]
        x.append(x_k - (a - x_k) * f(x_k) / (f(a) - f(x_k)))

    while abs(x[-1] - x[-2]) >= CONST_RATE:
        if f(b) * ddf(b) > 0:
            x_k = x[-1]
            x.append(x_k - (b - x_k) * f(x_k) / (f(b) - f(x_k)))
        else:
            a, b = b, a
    print("Метод хорд:")
    print("".join(format(i, '.7f') + "\n" for i in x))


# Метод секущих
def secant():
    x = [0.1, 1.1]

    while abs(x[-1] - x[-2]) >= CONST_RATE:
        x_k = x[-1]
        x_k_2 = x[-2]
        x.append(x_k - (x_k - x_k_2) * f(x_k) / (f(x_k) - f(x_k_2)))
    print("Метод секущих:")
    print("".join(format(i, '.7f') + "\n" for i in x))


# Конечноразностный метод Ньютона, h > 0 - малый параметр
def newton_diff(h):
    x = [0.1]
    x_k = x[-1]
    x.append(x_k - h * f(x_k) / (f(x_k + h) - f(x_k)))
    while abs(x[-1] - x[-2]) >= CONST_RATE:
        x_k = x[-1]
        x.append(x_k - h * f(x_k) / (f(x_k + h) - f(x_k)))
    print("Конечноразностный метод Ньютона:")
    print("".join(format(i, '.7f') + "\n" for i in x))


# Метод Стеффенсена
def steffensen():
    x = [1]
    x_k = x[-1]
    x.append(x_k - (f(x_k) ** 2) / (f(x_k + f(x_k)) - f(x_k)))
    while abs(x[-1] - x[-2]) >= CONST_RATE:
        x_k = x[-1]
        x.append(x_k - (f(x_k) ** 2) / (f(x_k + f(x_k)) - f(x_k)))
    print("Метод Стеффенсена:")
    print("".join(format(i, '.7f') + "\n" for i in x))


# Метод простых итераций
def simple_iterations():
    t = 0.3  # подходит любое значение от 0 до 2 / df(CONST_B)
    x = [0.2]

    x_k = x[-1]
    x.append(x_k - t * f(x_k))
    while abs(x[-1] - x[-2]) >= CONST_RATE:
        x_k = x[-1]
        x.append(x_k - t * f(x_k))
    print("Метод простых итераций:")
    print("".join(format(i, '.7f') + "\n" for i in x))


# graph()
newton()
chords()
secant()
newton_diff(0.01)
steffensen()
simple_iterations()
