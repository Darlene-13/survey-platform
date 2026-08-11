import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class XmlParserService {
  parse<T>(xml: string): T {
    const document = new DOMParser().parseFromString(xml, 'application/xml');
    const error = document.querySelector('parsererror');
    if (error) throw new Error('The server returned invalid XML.');
    return this.elementToValue(document.documentElement) as T;
  }

  escape(value: unknown): string {
    return String(value ?? '')
      .replaceAll('&', '&amp;').replaceAll('<', '&lt;').replaceAll('>', '&gt;')
      .replaceAll('"', '&quot;').replaceAll("'", '&apos;');
  }

  private elementToValue(element: Element): unknown {
    const children = Array.from(element.children);
    const attributes = Object.fromEntries(
      Array.from(element.attributes).map(attribute => [this.camelCase(attribute.name), attribute.value])
    );
    if (!children.length) return Object.keys(attributes).length ? { ...attributes, value: element.textContent?.trim() ?? '' } : element.textContent?.trim() ?? '';

    const result: Record<string, unknown> = { ...attributes };
    for (const child of children) {
      const key = this.camelCase(child.tagName);
      const value = this.elementToValue(child);
      if (key in result) result[key] = Array.isArray(result[key]) ? [...result[key] as unknown[], value] : [result[key], value];
      else result[key] = value;
    }
    return result;
  }

  private camelCase(value: string): string {
    return value.replace(/[-_]([a-z])/g, (_, letter: string) => letter.toUpperCase());
  }
}
