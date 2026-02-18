using System;
using System.Collections.Generic;

namespace CurrencyConverter
{
    public delegate double CurrencyConverter(double amount, double exchangeRate);

    class Program
    {
        
        class Currency
        {
            public string Code { get; set; }
            public string Name { get; set; }
            public double Rate { get; set; } 
        }

        
        static double ConvertCurrency(double amount, double exchangeRate)
        {
            return amount * exchangeRate;
        }

        
        static Currency SelectCurrency(List<Currency> currencies, string message)
        {
            Console.WriteLine($"\n{message}");
            for (int i = 0; i < currencies.Count; i++)
            {
                Console.WriteLine($"{i + 1}. {currencies[i].Code} - {currencies[i].Name} (Курс: {currencies[i].Rate})");
            }

            int choice;
            while (true)
            {
                Console.Write("Ваш выбор: ");
                if (int.TryParse(Console.ReadLine(), out choice) && choice >= 1 && choice <= currencies.Count)
                {
                    return currencies[choice - 1];
                }
                Console.WriteLine("Неверный выбор. Пожалуйста, выберите номер из списка.");
            }
        }

        static void Main(string[] args)
        {
            
            List<Currency> currencies = new List<Currency>
            {
                new Currency { Code = "USD", Name = "Доллар США", Rate = 1.17 },
                new Currency { Code = "EUR", Name = "Евро", Rate = 1.00},
                new Currency { Code = "RUB", Name = "Российский рубль", Rate = 90.87 },
                new Currency { Code = "KZT", Name = "Казахстанский тенге", Rate = 579.97 },
                new Currency { Code = "CNY", Name = "Китайский юань", Rate = 8.18 }
            };

            CurrencyConverter converter = new CurrencyConverter(ConvertCurrency);

            Console.Write("Введите ваш текущий баланс: ");
            double balance = double.Parse(Console.ReadLine());

            Console.Write("Введите сумму для перевода: ");
            double  amountToTransfer = double.Parse(Console.ReadLine());

            if (amountToTransfer > balance)
            {
                Console.WriteLine("Ошибка: недостаточно средств на счете!");
                return;
            }

            
            Currency sourceCurrency = SelectCurrency(currencies, "Выберите исходную валюту (валюту вашего счета):");

          
            Currency targetCurrency = SelectCurrency(currencies, "Выберите валюту для перевода:");

            double remainingBalance = balance - amountToTransfer;

      
            double amountInUSD = amountToTransfer / sourceCurrency.Rate;
            double  convertedAmount = converter(amountInUSD, targetCurrency.Rate);

           
            Console.WriteLine("РЕЗУЛЬТАТ ОПЕРАЦИИ:");

            Console.WriteLine($"Остаток на вашем счете ({sourceCurrency.Code}): {remainingBalance:F2}");
            Console.WriteLine($"Сумма после конвертации ({targetCurrency.Code}): {convertedAmount:F2}");

            Console.WriteLine($"\nДетали операции:");
            Console.WriteLine($"- Списано со счета: {amountToTransfer:F2} {sourceCurrency.Code}");
            Console.WriteLine($"- Зачислено: {convertedAmount:F2} {targetCurrency.Code}");
            Console.WriteLine($"- Курс конвертации: 1 {sourceCurrency.Code} = {targetCurrency.Rate / sourceCurrency.Rate:F4} {targetCurrency.Code}");

            Console.WriteLine("\nНажмите любую клавишу для выхода...");
            Console.ReadKey();
        }
    }
}
